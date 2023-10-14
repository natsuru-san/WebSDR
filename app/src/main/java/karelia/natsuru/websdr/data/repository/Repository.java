//Author - Natsuru-san (natsuru-san@mail.com)
//Created 01.10.2023

package karelia.natsuru.websdr.data.repository;

import java.util.List;

public interface Repository<T> {
    void save(T entity);
    void remove(long id);
    List<T> getAll();
}