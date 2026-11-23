package com.vinovim.app.features.updater

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LinuxUpdateInstallTest {

    // The asset names of a real release, in the order the GitHub API returns them.
    private val releaseAssets = listOf(
        asset("vinovim-Linux-x86_64-0.1.22-alpha.AppImage"),
        asset("vinovim-Linux-x86_64-0.1.22-alpha.AppImage.zsync"),
        asset("vinovim-Linux-x86_64-0.1.22-alpha.deb"),
        asset("vinovim-Linux-x86_64-0.1.22-alpha.flatpak"),
        asset("vinovim-Linux-x86_64-0.1.22-alpha.rpm"),
        asset("vinovim-macOS-arm64-0.1.22-alpha.dmg"),
        asset("vinovim-macOS-x86_64-0.1.22-alpha.dmg"),
        asset("vinovim-Windows-x64-0.1.22-alpha.msi"),
    )

    @Test
    fun `rpm install is offered the rpm asset`() {
        assertEquals(
            "vinovim-Linux-x86_64-0.1.22-alpha.rpm",
            selectFor(LinuxInstallMethod.RPM)?.name,
        )
    }

    @Test
    fun `deb install is offered the deb asset`() {
        assertEquals(
            "vinovim-Linux-x86_64-0.1.22-alpha.deb",
            selectFor(LinuxInstallMethod.DEB)?.name,
        )
    }

    @Test
    fun `app image install is offered the image itself and never its zsync file`() {
        assertEquals(
            "vinovim-Linux-x86_64-0.1.22-alpha.AppImage",
            selectFor(LinuxInstallMethod.APP_IMAGE)?.name,
        )
    }

    @Test
    fun `app image env wins over every other marker`() {
        val method = resolveLinuxInstallMethod(
            appImagePath = "/home/user/Apps/vinovim.AppImage",
            appImageExists = true,
            flatpakId = "com.vinovim.media.desktop",
            flatpakInfoExists = true,
            launcherPath = "/opt/vinovim/bin/vinovim",
            isOwnedByRpm = { true },
            isOwnedByDpkg = { true },
        )

        assertEquals(LinuxInstallMethod.APP_IMAGE, method)
    }

    @Test
    fun `a stale app image path does not claim the install`() {
        val method = resolveLinuxInstallMethod(
            appImagePath = "/home/user/Apps/deleted.AppImage",
            appImageExists = false,
            flatpakId = null,
            flatpakInfoExists = false,
            launcherPath = "/opt/vinovim/bin/vinovim",
            isOwnedByRpm = { true },
            isOwnedByDpkg = { false },
        )

        assertEquals(LinuxInstallMethod.RPM, method)
    }

    @Test
    fun `flatpak is recognised from its sandbox marker file alone`() {
        val method = resolveLinuxInstallMethod(
            appImagePath = null,
            appImageExists = false,
            flatpakId = null,
            flatpakInfoExists = true,
            launcherPath = "/app/opt/vinovim/bin/vinovim",
            isOwnedByRpm = { false },
            isOwnedByDpkg = { false },
        )

        assertEquals(LinuxInstallMethod.FLATPAK, method)
    }

    @Test
    fun `dpkg ownership is only consulted when rpm does not claim the launcher`() {
        var rpmQueried = false
        var dpkgQueried = false

        val method = resolveLinuxInstallMethod(
            appImagePath = null,
            appImageExists = false,
            flatpakId = null,
            flatpakInfoExists = false,
            launcherPath = "/opt/vinovim/bin/vinovim",
            isOwnedByRpm = { rpmQueried = true; false },
            isOwnedByDpkg = { dpkgQueried = true; true },
        )

        assertEquals(LinuxInstallMethod.DEB, method)
        assertTrue(rpmQueried)
        assertTrue(dpkgQueried)
    }

    @Test
    fun `an unknown launcher is never handed to a package manager`() {
        var queried = false

        val method = resolveLinuxInstallMethod(
            appImagePath = null,
            appImageExists = false,
            flatpakId = null,
            flatpakInfoExists = false,
            launcherPath = null,
            isOwnedByRpm = { queried = true; true },
            isOwnedByDpkg = { queried = true; true },
        )

        assertEquals(LinuxInstallMethod.UNKNOWN, method)
        assertFalse(queried)
    }

    @Test
    fun `the jpackage launcher is preferred over the process command`() {
        assertEquals(
            "/opt/vinovim/bin/vinovim",
            linuxLauncherPathFrom(
                jpackageAppPath = "/opt/vinovim/bin/vinovim",
                processCommand = "/usr/lib/jvm/java-17-openjdk/bin/java",
            ),
        )
    }

    // rpm -qf answers for the distribution's own java package, so trusting the
    // JVM path would report a source run as a packaged install.
    @Test
    fun `a jvm process command is not treated as the installed launcher`() {
        assertNull(
            linuxLauncherPathFrom(
                jpackageAppPath = null,
                processCommand = "/usr/lib/jvm/java-17-openjdk/bin/java",
            ),
        )
    }

    @Test
    fun `a real launcher process command is still used when jpackage is silent`() {
        assertEquals(
            "/opt/vinovim/bin/vinovim",
            linuxLauncherPathFrom(
                jpackageAppPath = "   ",
                processCommand = "/opt/vinovim/bin/vinovim",
            ),
        )
    }

    @Test
    fun `an unresolved install keeps the previous asset list`() {
        assertEquals(
            listOf(".deb", ".AppImage"),
            linuxUpdateFileExtensions(LinuxInstallMethod.UNKNOWN),
        )
    }

    @Test
    fun `a package install is handed to the desktop package handler`() {
        val command = linuxInstallerCommand(
            method = LinuxInstallMethod.RPM,
            updateFile = File("/tmp/updates/vinovim.rpm"),
            appImagePath = null,
            currentPid = 4242L,
        )

        assertEquals(listOf("xdg-open", "/tmp/updates/vinovim.rpm"), command)
    }

    @Test
    fun `an app image replaces itself once this process is gone`() {
        val command = linuxInstallerCommand(
            method = LinuxInstallMethod.APP_IMAGE,
            updateFile = File("/tmp/updates/vinovim.AppImage"),
            appImagePath = "/home/user/Apps/vinovim.AppImage",
            currentPid = 4242L,
        )

        assertEquals("sh", command[0])
        assertEquals("-c", command[1])
        val script = command[2]
        assertTrue(script.contains("kill -0 4242"))
        assertTrue(script.contains("cp -f -- '/tmp/updates/vinovim.AppImage' '/home/user/Apps/vinovim.AppImage'"))
        assertTrue(script.contains("exec '/home/user/Apps/vinovim.AppImage'"))
        assertTrue(script.contains("else xdg-open '/tmp/updates/vinovim.AppImage'"))
    }

    @Test
    fun `an app image with no known path falls back to the desktop handler`() {
        val command = linuxInstallerCommand(
            method = LinuxInstallMethod.APP_IMAGE,
            updateFile = File("/tmp/updates/vinovim.AppImage"),
            appImagePath = null,
            currentPid = 4242L,
        )

        assertEquals(listOf("xdg-open", "/tmp/updates/vinovim.AppImage"), command)
    }

    @Test
    fun `a quote in the app image path cannot break out of the script`() {
        val script = appImageReplaceScript(
            downloadedPath = "/tmp/updates/vinovim.AppImage",
            appImagePath = "/home/user/Ar'jun/vinovim.AppImage",
            currentPid = 7L,
        )

        assertTrue(script.contains("'/home/user/Ar'\\''jun/vinovim.AppImage'"))
        assertFalse(script.contains("Ar'jun"))
    }

    private fun selectFor(method: LinuxInstallMethod): AppUpdateAssetCandidate? =
        selectBestUpdateAsset(
            assets = releaseAssets,
            selector = AppUpdateAssetSelector(
                fileExtensions = linuxUpdateFileExtensions(method),
                preferredNameFragments = listOf("x64", "x86_64", "amd64", "linux"),
                fallbackNameFragments = listOf("universal", "all"),
            ),
        )

    private fun asset(name: String): AppUpdateAssetCandidate =
        AppUpdateAssetCandidate(
            name = name,
            downloadUrl = "https://example.test/$name",
        )
}
