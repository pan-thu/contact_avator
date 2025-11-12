package dev.panthu.contactavatorapplication

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy

/**
 * Application class for ContactAvatar+ with optimized Coil image loading configuration.
 *
 * Performance Optimizations:
 * - Memory cache: 25% of available memory
 * - Disk cache: 10% of available storage (max 250MB)
 * - Aggressive caching policies for optimal performance
 * - Crossfade animations enabled by default
 */
class ContactAvatarApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        // Application initialization
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25) // 25% of available memory
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.10) // 10% of available storage
                    .maxSizeBytes(250 * 1024 * 1024) // Max 250MB
                    .build()
            }
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .respectCacheHeaders(false) // Always cache locally
            .build()
    }
}
