import androidx.compose.runtime.BroadcastFrameClock
import androidx.compose.runtime.MonotonicFrameClock
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSRunLoop
import platform.Foundation.NSSelectorFromString
import platform.QuartzCore.CADisplayLink

@OptIn(ExperimentalForeignApi::class)
public object DisplayLinkClock3 : MonotonicFrameClock {

    @Suppress("unused") // This registers a DisplayLink listener.
    private val displayLink: CADisplayLink = CADisplayLink.displayLinkWithTarget(
        target = this,
        selector = NSSelectorFromString(this::tickClock.name),
    )

    private val clock = BroadcastFrameClock {
        // We only want to listen to the DisplayLink run loop if we have frame awaiters.
        displayLink.addToRunLoop(NSRunLoop.currentRunLoop, NSRunLoop.currentRunLoop.currentMode)
    }

    override suspend fun <R> withFrameNanos(onFrame: (frameTimeNanos: Long) -> R): R {
        return clock.withFrameNanos(onFrame)
    }

    // The following function must remain public to be a valid candidate for the call to
    // NSSelectorString above.
    @ObjCAction
    public fun tickClock() {
        clock.sendFrame(0L)

        // Remove the DisplayLink from the run loop. It will get added again if new frame awaiters
        // appear.
        displayLink.removeFromRunLoop(NSRunLoop.currentRunLoop, NSRunLoop.currentRunLoop.currentMode)
    }
}
