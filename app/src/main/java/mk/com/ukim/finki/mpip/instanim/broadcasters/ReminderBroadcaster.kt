package mk.com.ukim.finki.mpip.instanim.broadcasters

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import mk.com.ukim.finki.mpip.instanim.R
import mk.com.ukim.finki.mpip.instanim.SplashScreenActivity

class ReminderBroadcaster: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val tmpIntent = Intent(context, SplashScreenActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, tmpIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        val builder: NotificationCompat.Builder? = context?.let {
            NotificationCompat.Builder(it, "Instanim")
                .setSmallIcon(R.mipmap.ic_launcher_custom)
                .setContentTitle("Checkout new posts")
                .setContentText("There are new posts and that you should check out.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }

        with(context?.let { NotificationManagerCompat.from(it) }){
            builder?.build()?.let { this?.notify(0, it) }
        }
    }
}