package no.insane.insane.handlers;
import com.ramblingwood.minecraft.jsonapi.api.APIMethodName;

public interface JSONAPICallHandler {

    public boolean willHandle(APIMethodName methodName);

    public Object handle(APIMethodName methodName, Object[] args);
    
}