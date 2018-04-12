import disk.Cylinder;
import disk.LDisk;
import disk.Track;

import java.util.Arrays;


public class IOSystem {

    /**
     * ldisk - the physical disk
     * <p>
     * (L) - numberOfBlocks - is the number of logical blocks
     * <p>
     * (B) - blockLengthInBytes  - is the block length, i.e., the number of bytes per block.
     */
    private LDisk ldisk;
    private final int numberOfBlocks;
    private static final int blockLengthInBytes = 64;
    private final int numOfBlocksInOneCylinder;
    private final int numOfBlocksInOneTrack;
    private final int numOfBlocksInOneSector;

    public IOSystem(LDisk ldisk) {
        this.ldisk = ldisk;

        System.out.println("Total number of bytes = " + LDisk.numOfBytes);
        numberOfBlocks = LDisk.numOfBytes / blockLengthInBytes;
        System.out.println("Block length in bytes = " + blockLengthInBytes + "\nNumber of blocks = " + numberOfBlocks);
        numOfBlocksInOneCylinder = (Cylinder.numOfBytes / blockLengthInBytes);
        numOfBlocksInOneTrack = numOfBlocksInOneCylinder / Cylinder.numOfTracks;
        numOfBlocksInOneSector = numOfBlocksInOneTrack / Track.numOfSectors;

        System.out.println("numOfBlocksInOneCylinder = " + numOfBlocksInOneCylinder + "\nnumOfBlocksInOneTrack = " + numOfBlocksInOneTrack + "\nnumOfBlocksInOneSector = " + numOfBlocksInOneSector);
    }

    public int getBlockLengthInBytes() {
        return blockLengthInBytes;
    }

    /**
     * @param i index of block
     * @return int array that contains cylinder number, track number, sector number on disk, that correspond to the block.
     */
    private int[] getBlockLocationOnDisk(int i) throws Exception {
        int cylinderNumber = -1;
        for (int k = 1; k <= LDisk.numOfCylinders; k++) {
            if (i <= k * numOfBlocksInOneCylinder - 1) {
                cylinderNumber = k - 1;
                break;
            }
        }

        if (cylinderNumber < 0) throw new Exception("cannot define cylinder number for block #" + i);


        int trackNumber = -1;
        int tempBlockNumber = i % numOfBlocksInOneCylinder;
        System.out.println("tempBlockNumber = " + tempBlockNumber);

        for (int k = 1; k <= Cylinder.numOfTracks; k++) {
            if (tempBlockNumber <= k * numOfBlocksInOneTrack - 1) {
                trackNumber = k - 1;
                break;
            }
        }

        if (trackNumber < 0) throw new Exception("cannot define track number for block #" + i);


        int sectorNumber = -1;
        tempBlockNumber %= numOfBlocksInOneTrack;
        System.out.println("tempBlockNumber = " + tempBlockNumber);

        for (int k = 1; k <= Track.numOfSectors; k++) {
            if (tempBlockNumber <= k * numOfBlocksInOneSector - 1) {
                sectorNumber = k - 1;
                break;
            }
        }

        if (sectorNumber < 0) throw new Exception("cannot define sector number for block #" + i);

        int[] result = new int[3];

        System.out.println("\ntry to get block #" + i + "\ncylinder number = " + cylinderNumber + "\ntrackNumber = " + trackNumber + "\nsector number = " + sectorNumber);

        result[0] = cylinderNumber;
        result[1] = trackNumber;
        result[2] = sectorNumber;

        return result;
    }

    /**
     * Copies the logical block ldisk[i] into main memory starting at the location
     * specified by the pointer p. The number of characters copied corresponds to the
     * block length, B (blockLengthInBytes).
     *
     * @param i the number of the logical block that should be read
     * @param p the pointer that specified the destination location in main memory for storage the block's copy
     * @throws Exception
     */
    public int[] read_block(int i, Byte[] p) throws Exception {
        if (0 > i || i >= numberOfBlocks)
            throw new IllegalArgumentException("(i) should be: (0 <= i || i < numberOfBlocks); i = " + i + "; numberOfBlocks = " + numberOfBlocks);
        if (p.length != blockLengthInBytes) throw new IllegalArgumentException("Byte[] p.length != blockLengthInBytes");

        System.out.println("\nread block(i)\ni = " + i);

        int[] blockLocation = getBlockLocationOnDisk(i);


        System.out.println(Arrays.toString(blockLocation));
        return blockLocation;
    }

    /**
     * Copies the number of character corresponding to the block length, B (blockLengthInBytes), from
     * main memory starting at the location specified by the pointer p, into the logical
     * block ldisk[i].
     *
     * @param i the number of the destination logical block to which the block should be written
     * @param p the pointer that specified the source location in main memory from which block will be copied
     */
    public void write_block(int i, Byte[] p) throws IllegalArgumentException {
        if (0 > i || i >= numberOfBlocks)
            throw new IllegalArgumentException("(i) should be: (0 <= i || i < numberOfBlocks); i = " + i + "; numberOfBlocks = " + numberOfBlocks);

    }
}
