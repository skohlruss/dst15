package dst.ass2.di.type.extended;

import dst.ass2.di.annotation.Component;
import dst.ass2.di.annotation.ComponentId;
import dst.ass2.di.model.ScopeType;

/**
 * Created by pavol on 29.4.2015.
 */
@Component(scope = ScopeType.PROTOTYPE)
public class InvalidId {
    @ComponentId
    public int pk;
}
