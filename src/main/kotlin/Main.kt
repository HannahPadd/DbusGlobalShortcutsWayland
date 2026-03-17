import dev.hannah.portals.GlobalShortcutsHandler
import dev.hannah.portals.ShortcutTuple
import org.freedesktop.dbus.types.Variant

fun main() {
    val appId = "MyAppID${System.currentTimeMillis()}"
    val globalShortcutsHandler = GlobalShortcutsHandler(appId)

    val shortcut = mutableListOf(ShortcutTuple("Full_Reset", mapOf("description" to Variant("Yaw Reset"))))

    globalShortcutsHandler.createSession()
    globalShortcutsHandler.bindShortcut(shortcut)
    Thread.sleep(20000)
    globalShortcutsHandler.close()
}