package com.example.cryptocurrencytracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cryptocurrencytracker.domain.model.PricePoint
import com.example.cryptocurrencytracker.ui.theme.CryptocurrencyTrackerTheme

@Composable
fun PriceChart(
    points: List<PricePoint>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(modifier = modifier) {
        if (points.size < 2) return@Canvas

        val strokeWidth = 3.dp.toPx()
        val horizontalInset = strokeWidth / 2f
        val verticalInset = strokeWidth * 2f

        val chartWidth = size.width - horizontalInset * 2f
        val chartHeight = size.height - verticalInset * 2f

        val minTime = points.first().timestamp
        val timeRange = (points.last().timestamp - minTime).toFloat()
        val minPrice = points.minOf { it.price }
        val priceRange = points.maxOf { it.price } - minPrice

        val path = Path()
        points.forEachIndexed { index, point ->
            val x = if (timeRange == 0f) {
                horizontalInset
            } else {
                horizontalInset + (point.timestamp - minTime).toFloat() / timeRange * chartWidth
            }
            val y = if (priceRange == 0.0) {
                size.height / 2f
            } else {
                verticalInset + (1f - ((point.price - minPrice) / priceRange).toFloat()) * chartHeight
            }
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

private fun previewPoints(count: Int, price: (Int) -> Double): List<PricePoint> =
    List(count) { i ->
        PricePoint(timestamp = 1_700_000_000_000L + i * 3_600_000L, price = price(i))
    }

@Preview(name = "Рост", showBackground = true)
@Composable
private fun PriceChartUpPreview() {
    CryptocurrencyTrackerTheme {
        PriceChart(
            points = previewPoints(24) { 66_000.0 + it * 60 + if (it % 2 == 0) 150 else -150 },
            modifier = Modifier.fillMaxWidth().height(160.dp)
        )
    }
}

@Preview(name = "Падение", showBackground = true)
@Composable
private fun PriceChartDownPreview() {
    CryptocurrencyTrackerTheme {
        PriceChart(
            points = previewPoints(24) { 68_000.0 - it * 70 },
            modifier = Modifier.fillMaxWidth().height(160.dp)
        )
    }
}

@Preview(name = "Плоский", showBackground = true)
@Composable
private fun PriceChartFlatPreview() {
    CryptocurrencyTrackerTheme {
        PriceChart(
            points = previewPoints(24) { 1.0 },
            modifier = Modifier.fillMaxWidth().height(160.dp)
        )
    }
}

@Preview(name = "Две точки", showBackground = true)
@Composable
private fun PriceChartTwoPointsPreview() {
    CryptocurrencyTrackerTheme {
        PriceChart(
            points = previewPoints(2) { 66_000.0 + it * 500 },
            modifier = Modifier.fillMaxWidth().height(160.dp)
        )
    }
}