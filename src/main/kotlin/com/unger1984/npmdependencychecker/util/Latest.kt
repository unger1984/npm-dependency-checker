package com.unger1984.npmdependencychecker.util

import com.intellij.json.psi.JsonProperty

class Latest(pkg: JsonProperty, pkgName: String, version: String) {
    var pkg: JsonProperty = pkg

    var pkgName: String = pkgName

    var version: String = version
}
