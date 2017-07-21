package object types {
  type Slot = Int
  type ProcessID = String
  type AcceptedMap = Map[Slot, (BallotNumber, Value)]
  type SlotValueMap = Map[Slot, Value]
}
