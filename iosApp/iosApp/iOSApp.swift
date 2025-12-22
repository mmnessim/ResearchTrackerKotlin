import SwiftUI

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    @State private var detailsId: NSNumber?

    var body: some Scene {
        WindowGroup {
            ContentView(detailsId: detailsId)
                .onReceive(NotificationCenter.default.publisher(for: .navigateToDetails)) { notification in
                    if let id = notification.object as? NSNumber {
                        print(id)
                        detailsId = id
                    }
                }
        }
    }
}