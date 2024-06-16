import { Group } from "./group";

export class Event {
    private id: number | null;
    private celebratedId: number | null;
    private collectorId: number | null;
    private creationDate: string | null;
    private collectedAmount: number;
    private collectingGroup: Group | null;

    constructor(id: number | null = null, celebratedId: number | null = null, collectorId: number | null = null, creationDate: string | null = null, collectedAmount: number = 0, collectingGroup: Group | null = null) {
        this.id = id;
        this.celebratedId = celebratedId;
        this.collectorId = collectorId;
        this.creationDate = creationDate;
        this.collectedAmount = collectedAmount;
        this.collectingGroup = collectingGroup;
    }
}
