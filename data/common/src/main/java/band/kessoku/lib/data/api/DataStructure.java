package band.kessoku.lib.data.api;

/**
 * This is an interface used to abstract a structure holding some data and implement processing logic.
 * <p>
 *     When building structure, data must be member variable and {@link DataStructure#integrate(Data) "integrate()"}
 *     can be used to hold data for other usages.
 * </p>
 * @see AbstractDataStructure There also is an abstract structure for using.
 */
public interface DataStructure {
    <T> void integrate(Data<T> data);
    void integrate(DataStructure dataStructure);
}
