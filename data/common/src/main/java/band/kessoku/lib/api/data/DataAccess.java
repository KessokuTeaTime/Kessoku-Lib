package band.kessoku.lib.api.data;

@FunctionalInterface
public interface DataAccess<T, S extends DataStructure> {
    S get(T target);
}
