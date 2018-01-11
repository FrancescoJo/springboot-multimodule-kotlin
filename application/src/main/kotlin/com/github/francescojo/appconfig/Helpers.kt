package com.github.francescojo.appconfig

import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.support.ResourcePatternUtils
import java.io.IOException

@Throws(IOException::class)
internal fun loadResources(rl: ResourceLoader, resourcePath: String) =
        ResourcePatternUtils.getResourcePatternResolver(rl).getResources(resourcePath).toList()
