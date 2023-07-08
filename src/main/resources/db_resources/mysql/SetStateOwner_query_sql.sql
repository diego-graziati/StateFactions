UPDATE sf_citizenship
SET IsStateOwner=?, IsClaimResponsible=?
WHERE PersonId=?
      AND StateId=?;