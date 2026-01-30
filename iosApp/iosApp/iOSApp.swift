import SwiftUI

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    @State private var detailsId: NSNumber? = nil

    init() {
        // Read any pending details id persisted by AppDelegate (set when handling notification taps)
        if let pending = UserDefaults.standard.object(forKey: "pendingDetailsId") as? Int {
            if pending >= 0 {
                _detailsId = State(initialValue: NSNumber(value: pending))
            } else {
                _detailsId = State(initialValue: NSNumber(value: -1))
            }
            // Clear after reading so we don't re-consume it
            UserDefaults.standard.removeObject(forKey: "pendingDetailsId")
        }
    }

    var body: some Scene {
        WindowGroup {
            ContentView(detailsId: detailsId)
                .onReceive(NotificationCenter.default.publisher(for: .navigateToDetails)) { notification in
                    DispatchQueue.main.async {
                        if let id = notification.object as? NSNumber {
                            print(id)
                            detailsId = id
                        } else {
                            // nil indicates navigate home
                            detailsId = NSNumber(value: -1)
                        }
                    }
                }
        }
    }
}