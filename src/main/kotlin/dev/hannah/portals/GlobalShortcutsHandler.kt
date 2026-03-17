package dev.hannah.portals

import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.connections.impl.DBusConnectionBuilder
import org.freedesktop.dbus.interfaces.DBusSigHandler
import org.freedesktop.dbus.types.Variant
import java.util.concurrent.CountDownLatch

class GlobalShortcutsHandler(
    appId: String
) {
    private var sessionHandle: DBusPath? = null
    private val options = mutableMapOf<String, Variant<*>>()
    private val connection: DBusConnection = DBusConnectionBuilder.forSessionBus().build()
    private val globalShortcuts: GlobalShortcuts = connection.getRemoteObject(
        "org.freedesktop.portal.Desktop",
        "/org/freedesktop/portal/desktop",
        GlobalShortcuts::class.java
    )

    private lateinit var requestPath: DBusPath

    private val sessionReady = CountDownLatch(1)

    private val responseHandler = DBusSigHandler<Request.Response> { response ->
        println("dev.hannah.portals.Request path: $requestPath")
        if (response.path != requestPath.toString()) {
            return@DBusSigHandler
        }

        val sessionHandleResponse = response.results["session_handle"]?.value as String
        println("Session Handle Response $sessionHandleResponse")
        sessionHandle = DBusPath(sessionHandleResponse)

        sessionReady.countDown()
    }

    init {
        options["handle_token"] = Variant(appId)
        options["session_handle_token"] = Variant(appId)
        options["app_id"] = Variant(appId)

        connection.addSigHandler(Request.Response::class.java, responseHandler)
    }

    fun createSession() {
        requestPath = globalShortcuts.CreateSession(options)
        sessionReady.await()
    }

    fun bindShortcut(shortCutsList: MutableList<ShortcutTuple>) {
        globalShortcuts.BindShortcuts(sessionHandle, shortCutsList, "", mutableMapOf())
    }

    fun listShortcuts() {
        globalShortcuts.ListShortcuts(sessionHandle, mutableMapOf())
    }

    fun close() {
        connection.disconnect()
    }
}