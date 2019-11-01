package javax0.geci.mapper;

import javax0.geci.engine.Source;
import javax0.geci.mapper.sutclasses.MappedClass;
import javax0.geci.tools.CompoundParams;
import javax0.geci.tools.CompoundParamsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TestMapper {

    @Test
    @DisplayName("Properly generates toMap and fromMap")
    void testHappyPath() throws Exception {
        // GIVEN
        final var sut = Mapper.builder().build();
        final var source = Source.mock(sut).lines("package javax0.geci.mapper.sutclasses;\n" +
                                                      "\n" +
                                                      "import java.util.Map;\n" +
                                                      "import java.util.Set;\n" +
                                                      "\n" +
                                                      "public class MappedClass {\n" +
                                                      "    \n" +
                                                      "    private final String a = null;\n" +
                                                      "    protected int k;\n" +
                                                      "    public long z;\n" +
                                                      "    Map<Set<String>,String> mapp;\n" +
                                                      "    \n" +
                                                      "    //<editor-fold id=\"mapper\">\n" +
                                                      "    //</editor-fold>\n" +
                                                      "}\n")
                               .getSource();
        final CompoundParams global = new CompoundParamsBuilder("mapper").build();
        // WHEN
        sut.process(source, MappedClass.class, global);

        // THEN
        source.consolidate();
        Assertions.assertEquals("package javax0.geci.mapper.sutclasses;\n" +
                                    "\n" +
                                    "import java.util.Map;\n" +
                                    "import java.util.Set;\n" +
                                    "\n" +
                                    "public class MappedClass {\n" +
                                    "    \n" +
                                    "    private final String a = null;\n" +
                                    "    protected int k;\n" +
                                    "    public long z;\n" +
                                    "    Map<Set<String>,String> mapp;\n" +
                                    "    \n" +
                                    "    //<editor-fold id=\"mapper\">\n" +
                                    "    @javax0.geci.annotations.Generated(\"mapper\")\n" +
                                    "    public java.util.Map<String,Object> toMap() {\n" +
                                    "        return toMap0( new java.util.IdentityHashMap<>());\n" +
                                    "    }\n" +
                                    "    @javax0.geci.annotations.Generated(\"mapper\")\n" +
                                    "    public java.util.Map<String,Object> toMap0(java.util.Map<Object, java.util.Map> cache) {\n" +
                                    "        if( cache.containsKey(this) ){\n" +
                                    "            return cache.get(this);\n" +
                                    "        }\n" +
                                    "        final java.util.Map<String,Object> map = new java.util.HashMap<>();\n" +
                                    "        cache.put(this,map);\n" +
                                    "        map.put(\"a\",a);\n" +
                                    "        map.put(\"k\",k);\n" +
                                    "        map.put(\"mapp\",mapp);\n" +
                                    "        map.put(\"z\",z);\n" +
                                    "        return map;\n" +
                                    "    }\n" +
                                    "    @javax0.geci.annotations.Generated(\"mapper\")\n" +
                                    "    public static MappedClass fromMap(java.util.Map map) {\n" +
                                    "        return fromMap0(map,new java.util.IdentityHashMap<>());\n" +
                                    "    }\n" +
                                    "    public static MappedClass fromMap0(java.util.Map map,java.util.Map<java.util.Map,Object> cache) {\n" +
                                    "        if( cache.containsKey(map)){\n" +
                                    "            return (MappedClass)cache.get(map);\n" +
                                    "        }\n" +
                                    "        final MappedClass it = new MappedClass();\n" +
                                    "        cache.put(map,it);\n" +
                                    "        it.k = (int)map.get(\"k\");\n" +
                                    "        it.mapp = (java.util.Map)map.get(\"mapp\");\n" +
                                    "        it.z = (long)map.get(\"z\");\n" +
                                    "        return it;\n" +
                                    "    }\n" +
                                    "    //</editor-fold>\n" +
                                    "}",
            String.join("\n", source.getLines()));
    }

}
