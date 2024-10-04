package band.kessoku.lib.data.api;

/**
 * This is an interface used to abstract a structure holding some data and implement processing logic.
 * <p>
 *     When building structure, data must be member variable and {@link DataStructure#integrate(Data) "integrate()"}
 *     can be used to hold data for other usages.
 * </p>
 */
public interface DataStructure {
    <T, K extends Data<T>> K integrate(K data);
    <K extends DataStructure> K integrate(K dataStructure);
}
