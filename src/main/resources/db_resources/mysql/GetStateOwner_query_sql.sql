SELECT PersonId
FROM sf_citizenship
WHERE StateId=?
      AND IsStateOwner=true;