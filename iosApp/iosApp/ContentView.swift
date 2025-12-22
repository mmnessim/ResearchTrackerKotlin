import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    var detailsId: NSNumber?

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(detailsId: detailsId)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}

struct ContentView: View {
    var detailsId: NSNumber?

    var body: some View {
        ComposeView(detailsId: detailsId)
            .ignoresSafeArea()
    }
}