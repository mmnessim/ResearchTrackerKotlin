import UIKit
import UserNotifications
import Foundation
import ComposeApp

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
        if let detailsId = detailsId {
            NotificationCenter.default.post(name: .navigateToDetails, object: detailsId) // Unneeded??
            NavigationEvents.shared.triggerNavigateToDetails(id: KotlinLong(integerLiteral: (detailsId.intValue)))
            handleTap(response)
        } else {
            NavigationEvents.shared.triggerNavigateToDetails(id: KotlinLong(-1))
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