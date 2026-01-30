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

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        completionHandler([.banner, .sound, .badge])
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {
        let userInfo = response.notification.request.content.userInfo
        var detailsId: NSNumber? = nil
        if let id = userInfo["details_id"] as? NSNumber {
            detailsId = id
        } else if let idString = userInfo["details_id"] as? String, let id = Int(idString) {
            detailsId = NSNumber(value: id)
        }

        // Persist the tapped id so the SwiftUI app can read it if its subscriber hasn't been created yet.
        DispatchQueue.main.async {
            if let detailsId = detailsId {
                UserDefaults.standard.set(detailsId.intValue, forKey: "pendingDetailsId")
                NotificationCenter.default.post(name: .navigateToDetails, object: detailsId)
                self.handleTap(response)
            } else {
                // No details id -> navigate home; remove any pending key
                UserDefaults.standard.removeObject(forKey: "pendingDetailsId")
                NotificationCenter.default.post(name: .navigateToDetails, object: nil)
            }
        }

        completionHandler()
    }

    func handleTap(_ response: UNNotificationResponse) {
        print("Notification tapped: \(response.notification.request.identifier)")
    }
}


extension Notification.Name {
    static let navigateToDetails = Notification.Name("navigateToDetails")
}