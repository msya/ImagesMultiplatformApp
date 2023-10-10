import UIKit
import SwiftUI
import shared

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        Main_iosKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        NavigationView {
            ComposeView()
               .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
               .padding(.horizontal)
               .navigationTitle("Birds")
               .navigationBarTitleDisplayMode(.large)
        }
    }
}
