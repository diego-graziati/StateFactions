SELECT DISTINCT StateId
FROM sf_state_space
WHERE BlockX1 BETWEEN ? AND ?
      AND BlockZ1 BETWEEN ? AND ?;