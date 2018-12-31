package org.venompvp.venom.commands.arguments;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;

public class FactionArgument extends Argument<Faction> {

    public FactionArgument(String check) {
        super(check);
    }

    @Override
    public Faction getValue() {
        return FactionColl.get().getByName(check);
    }

    @Override
    public boolean isArgumentType() {
        return FactionColl.get().getByName(check) != null;
    }

    @Override
    public String unableToParse() {
        return check + " is unable to be parsed as a Faction";
    }
}
