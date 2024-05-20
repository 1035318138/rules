package rules.method;

import org.springframework.stereotype.Service;

/**
 * @author Can.Ru
 */
@Service
public class OtherService {
    public boolean isGreater(int a, int b) {
        return a > b;
    }
}
