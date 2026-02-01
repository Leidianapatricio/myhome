package src.br.edu.ifpb.myhome.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Formata valores em Real (Brasil): R$ 1.234.567,89
 */
public final class FormatadorMoeda {

    private static final NumberFormat REAL = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private FormatadorMoeda() {}

    /** Retorna o valor formatado em Real, ex.: "R$ 350.000,00" */
    public static String formatarReal(double valor) {
        return REAL.format(valor);
    }
}
