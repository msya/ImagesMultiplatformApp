import androidx.compose.runtime.BroadcastFrameClock
import androidx.compose.runtime.MonotonicFrameClock

actual fun getDisplayFrameClock(): MonotonicFrameClock? {
    return BroadcastFrameClock()
}
