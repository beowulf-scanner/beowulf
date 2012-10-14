package integration.beowulf.scs.categorizers;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.nvarghese.beowulf.scs.categorizers.MetaCategorizer;

public class MetaCategorizerTest {

	@Test(groups = "Scs_integration_test")
	public void testInitCategorizers() {

		MetaCategorizer metaCategorizer = new MetaCategorizer();
		metaCategorizer.initCategorizers(null, null);

		Assert.assertNotNull(metaCategorizer.getCategorizers());
		Assert.assertTrue(metaCategorizer.getCategorizers().size() > 0);

	}

	@Test(groups = "Scs_integration_test")
	public void testInitTransactionCategorizers() {

		MetaCategorizer metaCategorizer = new MetaCategorizer();
		metaCategorizer.initTransactionCategorizers(null, null);

		Assert.assertNotNull(metaCategorizer.getTransactionCategorizers());
		Assert.assertTrue(metaCategorizer.getTransactionCategorizers().size() > 0);

	}

}
