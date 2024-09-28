module kessoku.lib.base {
    requires transitive org.jetbrains.annotations;
    requires transitive org.slf4j;
    exports band.kessoku.lib.api;
    exports band.kessoku.lib.impl.base;
    exports band.kessoku.lib.api.base;
}