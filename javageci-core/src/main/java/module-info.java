module geci.core {
    requires geci.tools;
    requires geci.annotation;
    requires jamal.engine;
    requires jamal.api;

    exports javax0.geci.factory;
    exports javax0.geci.builder;
    exports javax0.geci.delegator;
    exports javax0.geci.accessor;
    exports javax0.geci.equals;
    exports javax0.geci.fluent;
    exports javax0.geci.mapper;
}