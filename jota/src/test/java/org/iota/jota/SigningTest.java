package org.iota.jota;

import java.util.Arrays;

import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Bundle;
import org.iota.jota.pow.ICurl;
import org.iota.jota.pow.SpongeFactory;
import org.iota.jota.utils.Checksum;
import org.iota.jota.utils.Constants;
import org.iota.jota.utils.Converter;
import org.iota.jota.utils.IotaAPIUtils;
import org.iota.jota.utils.Signing;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author schierlm@gmx.de
 */
public class SigningTest {

    private static final String TEST_SEED = "IHDEENZYITYVYSPKAURUZAQKGVJEREFDJMYTANNXXGPZ9GJWTEOJJ9IPMXOGZNQLSNMFDSQOTZAEETUEA";
    private static final String FIRST_ADDR = "LXQHWNY9CQOHPNMKFJFIJHGEPAENAOVFRDIBF99PPHDTWJDCGHLYETXT9NPUVSNKT9XDTDYNJKJCPQMZCCOZVXMTXC";
    private static final String SIXTH_ADDR = "HLHRSJNPUUGRYOVYPSTEQJKETXNXDIWQURLTYDBJADGIYZCFXZTTFSOCECPPPPY9BYWPODZOCWJKXEWXDPUYEOTFQA";
    private static final String SIGNATURE1 = "PYWFM9MYTPNZ9HTLZBBB9CGQWKPALDUNAQYCAA9VMQ9UMBLLAXSPPHQSNAAKJA9MZBXBHBQBFFKMBSDHDTCVCDWLUYCEQ9YZJAJAXXXZHDWTSLWGIWRE9LJFVWAFUMOAGHDBHJQ9APNBLSX9GPTJNTO9SBJT9UKYCZXYAWVGXEBJANNWEWZSPRYHASHGIFUWOEHUFMP9MWQBYZOZESCPLVJUCWGLEJIDPMEVNPBITBNFSQ9GBWCDTQZOPLPXOWWNQAEIXQRWMHAQDH9C9KKHGNKAX9INMUVVGIK9TPGRHOMDFAB9VICYDMSHHDDBRSTEFSZXMXFJUQRRAFBSCNHSMKRNNTTCMBURKBGC9EDWKLPBSQAKYCUKKSZWRVURZGUA9QVSXXPICIYFHLPJSWEFBZPUTWWNIKSAJM9OMRFFQVFJZZHLQBSEYXM9CN9HCGHSJBTYDGWOQPXOPZZE9EPQAQFT9GDWZCSOPMZHYYZXDDZ9DJDLOOOTIFQANFANNAYVIRUNDXSB9XRNXJYRDBLTEDWSUOVISMCHGKD9KDRSFDWRSVZQQKGAMDXFAWBSLMTTUMH9RAUIVI9HJMTODACSOP9MLHOJMSIWQ9TTNGPXRNWRHLMEMAH9GZHJRNJHQNBBLWKFXIZBMGMATZIZBFDPAFDCLDIFFAIK9JUSFYYC9ANDGXCZFLZYGURTUI9SWYYRGDJAHXDDNHSJZBCENZUSQXSFZMTXSFLRK9RIYAUMHPBOBNOXCHDIMBGIBVOOHIDQ9ORHHDECDTREIEILWDUFMUWYMGIXBIKRZMKGXTYZTX9GKFP9AUXMTUUQXRHHKPYULGJFJLEEYCNKLOWULRIAFM9OYKEDFRXFVTSJMSEMOURCLNOIETIHEUCMPLWKDXDO9TAHVH99MKTBAAKCMYKLJUQIVLLSVTFUM9KDSIHYXYHPRLDADSLSSOIGLLXMPKTHS9YXUNMUTBTBPDWXA9GVTBGLTCLEZEUNNIRBBURDWOFFYXELPFSZRQARVRPHGETKJTRUZIFDDWBOHHGUZTODZFMOVMAGCYCTGBWSGAVZADIPIASCKTRKIUUMHNGUYZKDVOPKKHXD9EXVUVJ9YFNYMLIJLEEGPIZLFS9FIEMG9MIEO9FPW9JZEVDQOECMTESICSMVWXZNXXJILJLVQHEBHQWPOBHKEGRLFCPLB9ZECJOZDAB9DMU9UALBIQDABVDYRRTPMZOCQX9WNGXVNKQZWPA9ACVONQMRHQDPPIQTP9VKP9PAORNOFTZZWGC9RYBWSNLULZGYLMYIWWPDMOHPZTQWRPRCN9RAUOKDSCWBRI9NPUPLBILOZDOOPHSWQGJEGUYWAWJDEBLEOBSYYU9XSRPBHRUQXIDOWJZQQVJTMP9VLWLOGBK9FZFHYLJCNENDATNPSF99DFPVPTNNKIUMHRGEBJXNUVENAHYLFPPHYFTIKCB9DBVCCSJTDMOMISBAAEJVBVLHOADKNFG9NQGIGRDICQCWZVHGGXLTUNQKBUTLDWXIM9REWBLIXFBPTOXBLWBQQUSRLRDHTXQWARPMBQILAJSYLLTDAGTFPCXBCDITDOIZNGKPZQWWHJDZIPYCPFEYFD9CVXYOJHJNUNMCMSIAUVSKCACNNPGDYJJVTZOREJOPIBYCMBULMTSDTJPZNVNYQBQPPABOSSNZJKQQZ9LULSHJUBLHIFMYWSNPGUERCLVFV9LOEBJEERYHI9OMSMSCDFDLNHEMLQXNRJDYSNKTOYCPTAUWAWIGCPJKMAMGLXNBJMO9BZGFIHWDVJWYCNZZV9KBWIFQSMAXBPGVXDW9SLTHOLMJORRXZJSTNOQDRGNBLGTFCCNBJECYZGWTDRJKJRBAJRCULMOUBQJFWCLWMEWGAAVNZWMDWBYDKZMUCZAKXQLRQPIQJPMORKJXKSDTGXWDHAKUOSMXCFXWSZYWXODWFACBMFSWQFVMBELPZMISVWRQQQPNHOTWOEQQAQJDLXFEEBXLJQEECWG9ARRRDLTVBHTPARJMLOZHYWDCSXPTZCNZWTCRUJNZWKFZXAARPHFCBTLWSLERGJJMKIG9NEBADRMZWYNWIRGTMOBRKURUE9GDLRIEODY9BXJOZUVNCXKXFPFDXKUTMXZRJDOQ9YTV9BJDKGZBYTWGVPQQMNVCNARLPSRQWN9TRMHWLNEJZFTCSRD";
    private static final String SIGNATURE2 = "URKFKLNXFEKDOGSQVMAOPEDIWSMTCKJZ9KEVWYALY9JAO9KHUGNDTMGQLKQJUIPWDIVMPEDSVPLFMDCIXDDT9WBBRTFQENL9AXLSBYHINXCDYBFGRNKJDYHAQVJKWCVOYXHTNBEZUNLVMJLUMZYJFAOW9PVVMJZNZZFJQEQFELVFZVFVWPJ9WQZJLPSGBYECHXSFVFQJGUCPFXC9GATTILVCAANNHOYMLOYX9QSUPCERYCOXPACZEEGLREBRZWXGUTTVTHB9GBRCIFEOBPIRXXPQKRSODEHDSZXLGIKXUQWNTQKIOPVDVSIK9WJUAEFOJBU9MBPBSVYSCLBMINTT9ZCTREZSMSVOPXSZOMCGFEZKMOCNLJ9QUTAPKBHRIAIYLCHUQHOINKSCMXWZVDGDXHNJQXJHPCCGBEWROVKEPAPBFFRCAVXZWIRKCRAWYHIHMDXFAGDJQNJJPYSQUHKFOOCEVQOGRQEIOQFKZWUQ9XVRNXKGMJOQEZHQZXQABWUQRBKXWHYUXEAEMDGXVY9WS9VJOCMGBQASSRNKAYJPTSPQEMYSJMTCLMDQJKDPBGQZZSFBDOKHBYY9UDRXNKTPWBCQTVKUGMEDUXL9TTKPATNIKVAGHACHPFSCRYNIRJBQC9OADPGWBFYYARSVNQCGMYQGCYLZH9KLMUIJPCLPQVS9BORXCJBXPDECJGKDNOUYWTKKFLXZARWKGUSMVMXKJTMRYZRERFCFGTZFZFCAOQSZGPQJUEZUJLJPU9QPMJUTZNLMSMPRGIFHUUZHMPMRBEBATEIIWPCOIMWOYOG9NYFBYOWFDKRXOTREBU99GNCPXKOWGI99LNVPRFFF9FCLFXI9HMUFU9NRLNJVTFNUSUJTAVOG9GKUYYEXIM9HTPIDTWIGLKRAQPKMQVZAPYMPSQIOJ9JZBWDMQHDSSRSHNCWSAJCSRORSEXLLQNZUKPXPGRLYMXOXWCCWWSBALFLXPHSGFLTOAFWPETBKJUMBLHMSKYLPJT9EJAZCPPNZWKPVCGKDJCRCLBBIAKVDSNWGONPLKFAYXZDI9FKPHDPKCB9UUPXLJVQTXOAZOQDRNSONXDVSLQGZYRIPGREYHRAUOSBFZDZPZHFNMWCZQGPXCZVLNCSASB9RQDFHOYMUVYLFKOEEWNREYCDMCTZIAFBFKLKRQWZCJHQZCZGWXIFTKRVMPHMVHAABHBDEV9WDEZBR9FLXLNBVNYKUOUFJQKNZVZVGZDDTFYNYFUVRLZKOLXXQYNV9MDVBLZSERXPGYKRIEZQZD9IBKFDT9AIYGWJJCXFWDUDURGJQLXVEJAVEOMZUVVTNCVBXEVQRDQIEHDUCSLCIJUTSCLFXEGMFYP9YLXELCZPMTBZWBIODZCFNJLVWTPQGLMQIHIABAYGJFFMOEDTCXGEDTNXMVXZYFGXRKVVRTIZ9ISXTDHAFPEKQZSM9XXQLOYBLTMD9MBERBIBEJDEXGMOLDZPZVVEPIRKJBDPAKFAWJPTCJSHZPDUKZEEHRFLMZCUGCOWFJBSTDGPHUIXSPPPHRQARMCFMTWKYPJNJQV9VSFZ9EWB9GVEAFUXHWRNUXQLCSBWROOITBATWUXUYGSMGAXKGEBP9ZJWXQWHBVPOSLDHTWXUOFQNO9EXSYPQF9LQLQAFNRU9MTIIRQLBBBYKUPANWRQKGESFARQIRUTGFMZVUKHZJYKTYOARTDOBIYBFRHJWEFHCYVHRHTLTWBRMUDVIVQVNELQMQRXYDNGVSICZINWIZCIWVFXLYOLYKWDNWCWFZUXHUWOPRDHMTSXOZX9CVHANU9ZXTJOGKEPYR9CHGOTIUQSWIALAOIKHQFXWY9ZWTSZADVXJNNZOLSCXVVFBRHLRBTGMSZOYNIXTAMABKGJTLGTZKRHOPPJMNYIQNVKRGXUQDWYEIEZYM9CSXO9YLSBJLDJUWOLUXDEKBGGEIDEXFLZMESDOITNYTNRLGOMHJH9HOLXJABUNLXCZYTXFPZMHRJPLXSVPDBJBBZX9TBIMZZFZOXUSFEJYHEXPFXGJCQTBBLPEEWAPHUETGXSXYYAF9PCCCOONRMQGAPJ9JO9BZQ9QSKTPFFYIFVHSLAZY9CWYSIMKDOSLRKWBHPGJGVEJEEMLCCWXKSOCMBMZZZJWYBBXE9FTAYJALGWITJRXAXWZEXMECTZEEIWZPHYX";

    private static final String ADDR_SEED = "LIESNFZLPFNWAPWXBLKEABZEEWUDCXKTRKZIRTPCKLKWOMJSEREWKMMMODUOFWM9ELEVXADTSQWMSNFVD";
    private static final String ADDR_I0_S1 = "HIPPOUPZFMHJUQBLBVWORCNJWAOSFLHDWF9IOFEYVHPTTAAF9NIBMRKBICAPHYCDKMEEOXOYHJBMONJ9D";
    private static final String ADDR_I0_S2 = "BPYZABTUMEIOARZTMCDNUDAPUOFCGKNGJWUGUXUKNNBVKQARCZIXFVBZAAMDAFRS9YOIXWOTEUNSXVOG9";
    private static final String ADDR_I0_S3 = "BYWHJJYSHSEGVZKKYTJTYILLEYBSIDLSPXDLDZSWQ9XTTRLOSCBCQ9TKXJYQAVASYCMUCWXZHJYRGDOBW";
    private static final String ADDR_LS_I0_S1 = "VKPCVHWKSCYQNHULMPYDZTNKOQHZNPEGJVPEHPTDIUYUBFKFICDRLLSIULHCVHOHZRHJOHNASOFRWFWZC";
    private static final String ADDR_LS_I0_S2 = "PTHVACKMXOKIERJOFSRPBWCNKVEXQ9CWUTIJGEUORSKWEDDJCBFQCCBQZLTYXQCXEDWLTMRQM9OQPUGNC";
    private static final String ADDR_LS_I0_S3 = "AGSAAETPMSBCDOSNXFXIOBAE9MVEJCSWVP9PAULQ9VABOTWLDMXID9MXCCWQIWRTJBASWPIJDFUC9ISWD";


    @Test
    public void testAddressGeneration() throws ArgumentException {
        assertEquals(FIRST_ADDR, IotaAPIUtils.newAddress(TEST_SEED, 2, 0, true, null));
        assertEquals(SIXTH_ADDR, IotaAPIUtils.newAddress(TEST_SEED, 2, 5, true, null));

        assertEquals(ADDR_I0_S1, IotaAPIUtils.newAddress(ADDR_SEED, 1, 0, false, null));
        assertEquals(ADDR_I0_S2, IotaAPIUtils.newAddress(ADDR_SEED, 2, 0, false, null));
        assertEquals(ADDR_I0_S3, IotaAPIUtils.newAddress(ADDR_SEED, 3, 0, false, null));

        assertEquals(ADDR_LS_I0_S1, IotaAPIUtils.newAddress(ADDR_SEED + ADDR_SEED, 1, 0, false, null));
        assertEquals(ADDR_LS_I0_S2, IotaAPIUtils.newAddress(ADDR_SEED + ADDR_SEED, 2, 0, false, null));
        assertEquals(ADDR_LS_I0_S3, IotaAPIUtils.newAddress(ADDR_SEED + ADDR_SEED, 3, 0, false, null));
    }

    @Test
    public void testLongSeedKeyGeneration() throws ArgumentException {
        ICurl curl = SpongeFactory.create(SpongeFactory.Mode.KERL);
        Signing signing = new Signing(curl);
        String seed = "EV9QRJFJZVFNLYUFXWKXMCRRPNAZYQVEYB9VEPUHQNXJCWKZFVUCTQJFCUAMXAHMMIUQUJDG9UGGQBPIY";

        for(int i = Constants.MIN_SECURITY_LEVEL; i < Constants.MAX_SECURITY_LEVEL; i++) {
            int[] key1 = signing.key(Converter.trits(seed), 0, i);
            assertEquals(Constants.KEY_LENGTH * i, key1.length);
            int[] key2 = signing.key(Converter.trits(seed + seed), 0, i);
            assertEquals(Constants.KEY_LENGTH * i, key2.length );
            int[] key3 = signing.key(Converter.trits(seed + seed + seed), 0, i);
            assertEquals(Constants.KEY_LENGTH * i, key3.length );
        }
    }

    @Test
    public void testSigning() throws ArgumentException {
        // we can sign any hash, so for convenience we will sign the first
        // address of our test seed
        // (but remove the checksum) with the key of our fifth address
        String hashToSign = removeChecksum(FIRST_ADDR);
        Signing signing = new Signing();
        final int[] key = signing.key(Converter.trits(TEST_SEED), 5, 2);
        int[] normalizedHash = new Bundle().normalizedBundle(hashToSign);
        int[] signature = signing.signatureFragment(Arrays.copyOfRange(normalizedHash, 0, 27), Arrays.copyOfRange(key, 0, 6561));
        assertEquals(SIGNATURE1, Converter.trytes(signature));
        int[] signature2 = signing.signatureFragment(Arrays.copyOfRange(normalizedHash, 27, 27 * 2), Arrays.copyOfRange(key, 6561, 6561 * 2));
        assertEquals(SIGNATURE2, Converter.trytes(signature2));
    }

    @Test
    public void testKeyLength() throws ArgumentException {
        Signing signing = new Signing();
        int[] key = signing.key(Converter.trits(TEST_SEED), 5, 1);
        assertEquals(Constants.KEY_LENGTH, key.length);
        key = signing.key(Converter.trits(TEST_SEED), 5, 2);
        assertEquals(2 * Constants.KEY_LENGTH, key.length);
        key = signing.key(Converter.trits(TEST_SEED), 5, 3);
        assertEquals(3 * Constants.KEY_LENGTH, key.length);
    }

    @Test
    public void testVerifying() throws ArgumentException {
        assertTrue(new Signing().validateSignatures(removeChecksum(SIXTH_ADDR), new String[]{SIGNATURE1, SIGNATURE2}, removeChecksum(FIRST_ADDR)));
    }

    private String removeChecksum(String address) throws ArgumentException {
        assertTrue(Checksum.isValidChecksum(address));
        return address.substring(0, Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM);
    }
}
