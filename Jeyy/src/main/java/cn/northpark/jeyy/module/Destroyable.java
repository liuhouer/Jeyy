package cn.northpark.jeyy.module;

/**
 * Object which has resource to release should implement this interface.
 * 
 * @author bruce 
 */
public interface Destroyable {

    /**
     * Called when container destroy the object.
     */
    void destroy();

}
