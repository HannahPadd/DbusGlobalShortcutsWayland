import dev.hannah.portals.PortalManager
import dev.hannah.portals.globalShortcuts.ShortcutTuple
import org.freedesktop.dbus.types.Variant
import kotlin.test.Test

const val APP_ID = "dev.hannah.portals"

/*
Options for shortcut include
"description"
"preferred_trigger"
"trigger_description"
 */


class LibraryTest {

    @Test
    fun testCreateShortcut() {
        var isRunning = true
        val portalManager = PortalManager(APP_ID)
        val shortcutsList = mutableListOf(
            ShortcutTuple("FULL_RESET", mapOf("description" to Variant("Full Reset"), "preferred_trigger" to Variant("CTRL+ALT+SHIFT+Y"))),
            ShortcutTuple("YAW_RESET", mapOf("description" to Variant("Yaw Reset"), "preferred_trigger" to Variant("CTRL+ALT+SHIFT+U"))),
            ShortcutTuple("MOUNTING_RESET", mapOf("description" to Variant("Mounting Reset"), "preferred_trigger" to Variant("CTRL+ALT+SHIFT+I"))),
            ShortcutTuple("FEET_MOUNTING_RESET", mapOf("description" to Variant("Feet Mounting Reset"), "preferred_trigger" to Variant("CTRL+ALT+SHIFT+P"))),
            ShortcutTuple("PAUSE_TRACKING", mapOf("description" to Variant("Pause Tracking"), "preferred_trigger" to Variant("CTRL+ALT+SHIFT+O"))))
        val globalShortcutsHandler = portalManager.globalShortcutsRequest(shortcutsList)
        Runtime.getRuntime().addShutdownHook(Thread {
            println("Closing connection")
            globalShortcutsHandler.close()
        })

         while (isRunning) {
            globalShortcutsHandler.onShortcutActivated = { shortcutId ->
                when (shortcutId) {
                    "FULL_RESET" -> {
                        println("Full reset triggered")
                    }
                    "YAW_RESET" -> {
                        println("Yaw reset triggered")
                    }
                    "MOUNTING_RESET" -> {
                        println("Mounting reset triggered")
                    }
                    "FEET_MOUNTING_RESET" -> {
                        println("Feet mounting reset triggered")
                    }
                    "PAUSE_TRACKING" -> {
                        println("Pause tracking triggered")
                        isRunning = false
                    }
                }
            }

             globalShortcutsHandler.onShortcutsChanged = { shortcuts ->
                 for (shortcut in shortcuts) {
                     println("${shortcut.id} ${shortcut.options.values.first()} ${shortcut.options.values.last()}")
                 }
             }
        }
    }
}