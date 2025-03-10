package com.example.jobseeker.component

import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.load
import coil.size.Scale
import com.example.jobseeker.R

@Composable
fun CustomImageView(imageUrl: String) {
    val context = LocalContext.current

    val imageHeightPx = with(LocalDensity.current) { 100.dp.toPx().toInt() }
    val imageWidthPx = with(LocalDensity.current) { 100.dp.toPx().toInt() }

    AndroidView(factory = { context ->
        ImageView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT, // Width will be determined by content
//                100 // Fixed height
                imageWidthPx,
                imageHeightPx
            )

            // Set the image loading behavior
            load(imageUrl) {
                //crossfade(true)
                //placeholder(R.drawable.landscape_placeholder_svgrepo_com) // Placeholder resource
                scale(Scale.FIT)
            }
        }
    },
        modifier = Modifier
            .height(100.dp) // Fixed height
            .fillMaxWidth(0.5f)
    )

}