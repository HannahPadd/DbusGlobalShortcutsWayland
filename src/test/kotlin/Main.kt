import dev.hannah.portals.PortalManager
import dev.hannah.portals.globalShortcuts.ShortcutTuple
import org.freedesktop.dbus.types.Variant
import kotlin.test.Test

const val APP_ID = "dev.hannah.portals"


fun main() {
    LibraryTest().testCreateShortcut()
}

class LibraryTest {

    @Test()
    fun testCreateShortcut() {
        var isRunning = true
        val portalManager = PortalManager(APP_ID)
        val shortcutsList = mutableListOf(ShortcutTuple("YAW_RESET_5", mapOf("description" to Variant("Yaw Reset"))))
        val globalShortcutsHandler = portalManager.globalShortcutsRequest(shortcutsList)
        Runtime.getRuntime().addShutdownHook(Thread {
            println("Closing connection")
            globalShortcutsHandler.close()
        })

         while (isRunning) {
            globalShortcutsHandler.onShortcutActivated = { shortcutId ->
                when (shortcutId) {
                    "YAW_RESET_5" -> {
                        println("Yaw reset triggered")
                        isRunning = false
                    }
                }
            }
        }
    }
}