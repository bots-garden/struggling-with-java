package garden.bots.app;

import com.dylibso.chicory.wasm.types.Value;

import java.io.File;

import com.dylibso.chicory.runtime.*;
import com.dylibso.chicory.runtime.Module;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        File wasmFile = new File("./factorial.wasm");
        Module module = Module.build(wasmFile);
        Instance instance = module.instantiate();

        ExportFunction iterFact = instance.getExport("iterFact");
        Value result = iterFact.apply(Value.i32(5))[0];
        
        System.out.println(result.asInt());

    }
}