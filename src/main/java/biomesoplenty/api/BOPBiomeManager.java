package biomesoplenty.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.WeightedRandom;
import net.minecraft.world.biome.BiomeGenBase;
import biomesoplenty.common.configuration.BOPConfigurationBiomeGen;
import biomesoplenty.common.configuration.BOPConfigurationIDs;

public class BOPBiomeManager 
{
	private static int nextBiomeId = 40;
	
	public static List<BiomeEntry>[] overworldBiomes = new ArrayList[] { new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList()};
	public static List<BiomeEntry> netherBiomes = new ArrayList();
	
	public static BiomeGenBase createAndRegisterBiome(Class<? extends BiomeGenBase> biomeClass, String biomeType, String biomeName, List<BiomeEntry> biomeList, int weight)
	{
		BiomeGenBase biome = createBiome(biomeClass, biomeName);
		
		if (biome != null)
		{
			BiomeEntry entry = new BiomeEntry(biome, weight);

			if (BOPConfigurationBiomeGen.config.get(biomeType + " Biomes To Generate", biome.biomeName, true).getBoolean(false))
			{
				biomeList.add(entry);
			}

			return biome;
		}
		
		return null;
	}
	
	public static BiomeGenBase createBiome(Class<? extends BiomeGenBase> biomeClass, String biomeName)
	{
		int biomeId = BOPConfigurationIDs.config.get("Biome IDs", biomeName + " ID", getNextFreeBiomeId()).getInt();

		if (biomeId != -1)
		{ 
			try
			{
				BiomeGenBase biome = biomeClass.getConstructor(int.class).newInstance(biomeId).setBiomeName(biomeName);
				
				return biome;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return null;
	}
	
	public static int getNextFreeBiomeId()
	{
		for (int i = nextBiomeId; i < 256; i++)
		{
			if (BiomeGenBase.getBiomeGenArray()[i] != null) 
			{
				if (i == 255) throw new IllegalArgumentException("There are no more biome ids avaliable!");
				continue;
			}
			else
			{
				nextBiomeId = i + 1;
				return i;
			}
		}
		
		return -1;
	}
	
	public class TemperatureType
	{
		public static final int HOT = 0;
		public static final int WARM = 1;
		public static final int COOL = 2;
		public static final int ICY = 3;
	}
	
	public static class BiomeEntry extends WeightedRandom.Item
	{
		public BiomeGenBase biome;
		
		public BiomeEntry(BiomeGenBase biome, int weight)
		{
			super(weight);
			this.biome = biome;
		}
	}
}
