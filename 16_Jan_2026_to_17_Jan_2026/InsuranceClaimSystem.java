package collection1;

public class InsuranceClaimSystem  {

    public static void main(String[] args) {

        ClaimProcessor processor = new ClaimProcessor();

        InsuranceClaim claim1 = new HealthClaim("CL101", 300000);
        InsuranceClaim claim2 = new VehicleClaim("CL102", 800000);

        claim1.processClaim(processor);
        claim2.processClaim(processor);
    }
}

enum ClaimType {
    HEALTH(500000, 0.90),
    VEHICLE(600000, 0.70),
    TRAVEL(200000, 0.80);

    private final double maxCoverage;
    private final double payoutMultiplier;

    ClaimType(double maxCoverage, double payoutMultiplier) {
        this.maxCoverage = maxCoverage;
        this.payoutMultiplier = payoutMultiplier;
    }

    public boolean isValidAmount(double claimAmount) {
        return claimAmount <= maxCoverage;
    }

    public double calculatePayout(double claimAmount) {
        return claimAmount * payoutMultiplier;
    }
}

enum ClaimStatus {
    SUBMITTED {
        @Override
        public boolean canTransitionTo(ClaimStatus next) {
            return next == VERIFIED || next == REJECTED;
        }
    },
    VERIFIED {
        @Override
        public boolean canTransitionTo(ClaimStatus next) {
            return next == APPROVED || next == REJECTED;
        }
    },
    APPROVED {
        @Override
        public boolean canTransitionTo(ClaimStatus next) {
            return false;
        }
    },
    REJECTED {
        @Override
        public boolean canTransitionTo(ClaimStatus next) {
            return false;
        }
    };

    public abstract boolean canTransitionTo(ClaimStatus next);
}

abstract class InsuranceClaim {

    protected String claimId;
    protected double claimAmount;
    protected ClaimStatus status;
    protected ClaimType claimType;

    public InsuranceClaim(String claimId, double claimAmount, ClaimType claimType) {
        this.claimId = claimId;
        this.claimAmount = claimAmount;
        this.claimType = claimType;
        this.status = ClaimStatus.SUBMITTED;
    }

    public void updateStatus(ClaimStatus newStatus) {
        if (!status.canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                "Invalid status transition from " + status + " to " + newStatus
            );
        }
        this.status = newStatus;
    }

    public abstract void processClaim(ClaimProcessor processor);
}


class HealthClaim extends InsuranceClaim {

    public HealthClaim(String claimId, double claimAmount) {
        super(claimId, claimAmount, ClaimType.HEALTH);
    }

    @Override
    public void processClaim(ClaimProcessor processor) {
        if (!processor.validateClaim(this)) {
            updateStatus(ClaimStatus.REJECTED);
            System.out.println("Claim " + claimId + " rejected due to coverage limit");
            return;
        }

        updateStatus(ClaimStatus.VERIFIED);
        updateStatus(ClaimStatus.APPROVED);

        double payout = processor.calculatePayout(this);
        System.out.println("Claim " + claimId + " approved. Payout: " + (int)payout);
    }
}


class VehicleClaim extends InsuranceClaim {

    public VehicleClaim(String claimId, double claimAmount) {
        super(claimId, claimAmount, ClaimType.VEHICLE);
    }

    @Override
    public void processClaim(ClaimProcessor processor) {
        if (!processor.validateClaim(this)) {
            updateStatus(ClaimStatus.REJECTED);
            System.out.println("Claim " + claimId + " rejected due to coverage limit");
            return;
        }

        updateStatus(ClaimStatus.VERIFIED);
        updateStatus(ClaimStatus.APPROVED);

        double payout = processor.calculatePayout(this);
        System.out.println("Claim " + claimId + " approved. Payout: " + (int)payout);
    }
}
class TravelClaim extends InsuranceClaim {

    public TravelClaim(String claimId, double claimAmount) {
        super(claimId, claimAmount, ClaimType.TRAVEL);
    }

    @Override
    public void processClaim(ClaimProcessor processor) {
        if (!processor.validateClaim(this)) {
            updateStatus(ClaimStatus.REJECTED);
            System.out.println("Claim " + claimId + " rejected due to coverage limit");
            return;
        }

        updateStatus(ClaimStatus.VERIFIED);
        updateStatus(ClaimStatus.APPROVED);

        double payout = processor.calculatePayout(this);
        System.out.println("Claim " + claimId + " approved. Payout: " + (int)payout);
    }
}

class ClaimProcessor {

    public boolean validateClaim(InsuranceClaim claim) {
        return claim.claimType.isValidAmount(claim.claimAmount);
    }

    public double calculatePayout(InsuranceClaim claim) {
        return claim.claimType.calculatePayout(claim.claimAmount);
    }
}