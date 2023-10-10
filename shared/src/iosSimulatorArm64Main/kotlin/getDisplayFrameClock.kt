import androidx.compose.runtime.MonotonicFrameClock

actual fun getDisplayFrameClock(): MonotonicFrameClock? {
    return DisplayLinkClock3
}