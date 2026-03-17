import org.example.GlobalShortcutsHandler
import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.connections.impl.DBusConnectionBuilder
import org.freedesktop.dbus.types.Variant
import org.freedesktop.portal.GlobalShortcuts
import org.freedesktop.portal.ShortcutTuple

fun main() {
    val appId = "dev.slimevr.SlimeVR"
    val globalShortcutsHandler = GlobalShortcutsHandler(appId)

    val shortcut = mutableListOf<ShortcutTuple>(ShortcutTuple("Yaw_Reset", mapOf("description" to Variant("Yaw Reset"))))

    globalShortcutsHandler.createSession()
    globalShortcutsHandler.bindShortcut(shortcut)
}