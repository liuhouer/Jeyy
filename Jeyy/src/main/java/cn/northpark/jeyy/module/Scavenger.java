package cn.northpark.jeyy.module;

/**
 * 实现此接口的class会在生命周期结束后 自动销毁
 * @author bruce 
 */
public interface Scavenger {

    /**
     * Called when container destroy the object.
     */
    void destroy();

}
