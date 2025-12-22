import UIKit
import UserNotifications
import Foundation

class AppDelegate: UIResponder, UIApplicationDelegate, UNUserNotificationCenterDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        let center = UNUserNotificationCenter.current()
        center.delegate = self
        return true
    }

    // Show notifications while app is in foreground
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        completionHandler([.banner, .sound, .badge])
    }

    // Handle notification tap
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        let userInfo = response.notification.request.content.userInfo
        // Support both legacy and new keys for details ID
        var detailsId: NSNumber? = nil
        if let id = userInfo["id"] as? NSNumber {
            detailsId = id
        } else if let idString = userInfo["id"] as? String, let id = Int(idString) {
            detailsId = NSNumber(value: id)
        } else if let id = userInfo["details_id"] as? NSNumber {
            detailsId = id
        } else if let idString = userInfo["details_id"] as? String, let id = Int(idString) {
            detailsId = NSNumber(value: id)
        }
        if let detailsId = detailsId {
            NotificationCenter.default.post(name: .navigateToDetails, object: detailsId)
        }
        completionHandler()
    }
}


extension Notification.Name {
    static let navigateToDetails = Notification.Name("navigateToDetails")
}