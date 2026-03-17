package org.example

import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.connections.impl.DBusConnectionBuilder
import org.freedesktop.dbus.types.Variant
import org.freedesktop.portal.GlobalShortcuts
import org.freedesktop.portal.Request
import org.freedesktop.portal.ShortcutTuple
import java.util.concurrent.CountDownLatch

class GlobalShortcutsHandler(
    val appId: String
) {
    private var sessionHandle: DBusPath? = null
    private val options = mutableMapOf<String, Variant<*>>()
    private val connection: DBusConnection = DBusConnectionBuilder.forSessionBus().build()
    private val globalShortcuts: GlobalShortcuts = connection.getRemoteObject(
        "org.freedesktop.portal.Desktop",
        "/org/freedesktop/portal/desktop",
        GlobalShortcuts::class.java
    )

    private val sessionReady = CountDownLatch(1)
    private lateinit var requestPath: DBusPath

    init {
        options["handle_token"] = Variant(appId)
        options["session_handle_token"] = Variant(appId)
        options["app_id"] = Variant(appId)

        connection.addSigHandler(Request.Response::class.java) { response ->
            println("Request path: $requestPath")
            if (response.path != requestPath.toString()) {
                return@addSigHandler
            }

            val sessionHandleResponse = response.results["session_handle"]?.value as String
            println("Session Handle Response $sessionHandleResponse")
            sessionReady.countDown()
            sessionHandle = DBusPath(sessionHandleResponse)
        }
    }

    fun createSession() {
        Thread.sleep(200)
        requestPath = globalShortcuts.CreateSession(options)
    }

    fun bindShortcut(shortCutsList: MutableList<ShortcutTuple>) {
        sessionReady.await()
        globalShortcuts.BindShortcuts(sessionHandle, shortCutsList, "", mutableMapOf())
    }

    fun listShortcuts() {
        sessionReady.await()
        globalShortcuts.ListShortcuts(sessionHandle, mutableMapOf())
    }
}