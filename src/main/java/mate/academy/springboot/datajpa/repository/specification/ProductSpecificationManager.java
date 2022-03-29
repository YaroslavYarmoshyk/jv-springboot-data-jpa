package mate.academy.springboot.datajpa.repository.specification;

import mate.academy.springboot.datajpa.model.Product;
import mate.academy.springboot.datajpa.util.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ProductSpecificationManager implements SpecificationManager<Product> {
    private final Map<String, SpecificationProvider<Product>> providersMap;

    @Autowired
    public ProductSpecificationManager(List<SpecificationProvider<Product>> productSpecifications) {
        this.providersMap = productSpecifications.stream()
                .collect(Collectors.toMap(SpecificationProvider::getFilterKey, Function.identity()));
    }

    @Override
    public Specification<Product> get(String filterKey, String[] parameters) {
        if (!providersMap.containsKey(filterKey)) {
            throw new SystemException("Key " + filterKey + " is not supported for data filtering");
        }
        return providersMap.get(filterKey).getSpecification(parameters);
    }
}