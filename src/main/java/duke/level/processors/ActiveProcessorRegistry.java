package duke.level.processors;

import duke.level.LevelBuilder;

import java.util.List;

public class ActiveProcessorRegistry {
    private List<ActiveProcessor> processors;

    public ActiveProcessorRegistry(List<ActiveProcessor> processors) {
        this.processors = processors;
    }

    public ActiveProcessor getProcessor(int tileId) {
        return processors.stream().filter(processor -> processor.canProcess(tileId)).findFirst().orElse(NOOP);
    }

    public static ActiveProcessorRegistry createDefault() {
        return new ActiveProcessorRegistry(List.of(
                new PlayerStartProcessor(),
                new DecorationProcessor(),
                new AcmeProcessor(),
                new ReactorProcessor(),
                new SecurityCameraProcessor(),
                new BoxProcessor(),
                new ItemProcessor(),
                new SpikesProcessor(),
                new ElevatorProcessor(),
                new BridgeProcessor(),
                new DoorProcessor(),
                new LockProcessor(),
                new FlamethrowerProcessor(),
                new TechbotProcessor(),
                new WallCrawlerProcessor(),
                new BouncingMineProcessor(),
                new TankbotProcessor(),
                new Ed209Processor(),
                new ExitProcessor(),
                new NotesProcessor(),
                new NeedleProcessor(),
                new MonitorProcessor()
        ));
    }

    private static final ActiveProcessor NOOP = new ActiveProcessor() {
        @Override
        public boolean canProcess(int tileId) {
            return true;
        }

        @Override
        public void process(int index, int tileId, LevelBuilder builder) {
            System.err.printf("0x%04X is not mapped\n", tileId);
        }
    };
}
