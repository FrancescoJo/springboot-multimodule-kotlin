/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient.httpv1.dto.notification

/**
 * Notification to send to android devices.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 5 - Feb - 2018
 */
class AndroidNotification: Notification {
    private val _valueMap = HashMap<String, Any>()

    /**
     * The notification's title. If present, it will override
     * [google.firebase.fcm.v1.Notification.title](https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages#notification.FIELDS.title).
     */
    var title = ""

    /**
     * The notification's body text. If present, it will override
     * [google.firebase.fcm.v1.Notification.body](https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages#notification.FIELDS.body).
     */
    var body = ""

    /**
     * The notification's icon. Sets the notification icon to `myicon`
     * for drawable resource `myicon`. If you don't send this key in the
     * request, FCM displays the launcher icon specified in your app manifest.
     */
    var icon = ""

    /** The notification's icon color, expressed in `#rrggbb` format. */
    var color = ""

    /**
     * The sound to play when the device receives the notification. Supports
     * "default" or the filename of a sound resource bundled in the app.
     * Sound files must reside in `/res/raw/`.
     */
    var sound = ""

    /**
     * Identifier used to replace existing notifications in the
     * notification drawer.
     *
     * If not specified, each request creates a new notification.
     *
     * If specified and a notification with the same tag is
     * already being shown, the new notification replaces the existing
     * one in the notification drawer.
     */
    var tag = ""

    /**
     * The action associated with a user click on the notification.
     * If specified, an activity with a matching intent filter is
     * launched when a user clicks on the notification.
     */
    var clickAction = ""

    /**
     * The key to the body string in the app's string resources to use
     * to localize the body text to the user's current localization.
     * See [String Resources](https://goo.gl/NdFZGI) for more information.
     */
    var bodyLocaleKey = ""

    /**
     * Variable string values to be used in place of the format specifiers
     * in [bodyLocaleKey] to use to localize the body text to the user's
     * current localization. See [Formatting and Styling](https://goo.gl/MalYE3) for more information.
     */
    var bodyLocaleArgs: MutableSet<String> = HashSet()

    /**
     * The key to the title string in the app's string resources to use
     * to localize the title text to the user's current localization.
     * See [String Resources](https://goo.gl/NdFZGI) for more information.
     */
    var titleLocaleKey = ""

    /**
     * Variable string values to be used in place of the format specifiers
     * in [titleLocaleKey] to use to localize the title text to the user's
     * current localization. See [Formatting and Styling](https://goo.gl/MalYE3) for more information.
     */
    var titleLocaleArgs: MutableSet<String> = HashSet()

    override val valueMap: Map<String, Any>
        get() {
            return _valueMap.apply {
                clear()
                title.takeIf { it.isNotEmpty() }?.let { put("title", it) }
                body.takeIf { it.isNotEmpty() }?.let { put("body", it) }
                icon.takeIf { it.isNotEmpty() }?.let { put("icon", it) }
                color.takeIf { it.isNotEmpty() }?.let { put("color", it) }
                sound.takeIf { it.isNotEmpty() }?.let { put("sound", it) }
                tag.takeIf { it.isNotEmpty() }?.let { put("tag", it) }
                clickAction.takeIf { it.isNotEmpty() }?.let { put("click_action", it) }
                bodyLocaleKey.takeIf { it.isNotEmpty() }?.let { put("body_loc_key", it) }
                bodyLocaleArgs.takeIf { it.isNotEmpty() }?.let { put("body_loc_args", it) }
                titleLocaleKey.takeIf { it.isNotEmpty() }?.let { put("title_loc_key", it) }
                titleLocaleArgs.takeIf { it.isNotEmpty() }?.let { put("title_loc_args", it) }
            }
        }

    companion object {
        val EMPTY = AndroidNotification()
    }
}
