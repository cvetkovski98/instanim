package mk.com.ukim.finki.mpip.instanim

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import mk.com.ukim.finki.mpip.instanim.broadcasters.ReminderBroadcaster

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.postListFragment,
                R.id.profileListFragment,
                R.id.profileFragment,
                R.id.pictureSelectFragment,
                R.id.mapsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val intent = Intent(this, ReminderBroadcaster::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val time = System.currentTimeMillis()
        val tenSec = 1000 * 10
        alarmManager.set(AlarmManager.RTC_WAKEUP,
            time+tenSec,
            pendingIntent)
    }

    private fun createNotificationChannel() {
        val name = "InstanimReminderChannel"
        val description = "Channel for Instanim"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("Instanim", name, importance)
        channel.description = description

        val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }
}
