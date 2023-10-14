//Author - Natsuru-san (natsuru-san@mail.com)
//Created 14.10.2023

package karelia.natsuru.websdr.data.exceptions;

public final class NegativeLimitStations extends RuntimeException {
    public NegativeLimitStations(int limit) {
        super("The limit of stations is " + limit + " -> number cannot be zero or below!");
    }
}