package Shapes;

/**
 * Interface for objects that have a name.
 */
public interface INamed {
    /**
     * Returns the name of the object.
     * @return the name string
     */
    default String name(){
        return "";
    }
}
