package nl.ealse.ccnl.service;

@FunctionalInterface
public interface SearchItemHolder<T extends Enum<?>> {

  T[] getSearchItems();

}
